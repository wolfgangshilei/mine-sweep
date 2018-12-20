(ns mine-sweep.ui.game-panel.mine-field.events
  (:require [re-frame.core :as rf]
            [mine-sweep.utils.re-frame :refer [register-event-fx register-event-db]]
            [mine-sweep.ui.common.constants :as const]
            [mine-sweep.utils.log :refer [log]]))

(defn- init-cells-pos
  [n-row n-col]
  (for [x (range n-row)
        y (range n-col)]
    [x y]))

(defn- init-neighbours-for-cell
  [[x y] n-row n-col mine-cells-pos]
  (for [row ((juxt inc dec identity) x)
        col ((juxt inc dec identity) y)
        :when (and (>= row 0)
                   (>= col 0)
                   (< row n-row)
                   (< col n-col)
                   (or (not= row x)
                       (not= col y)))]
    [row col]))

(defn init-cells-data
  [& {:keys [n-row n-col mine-indexes]}]
  (let [cells-pos (init-cells-pos n-row n-col)
        mine-cells-pos (into #{} (map #(nth cells-pos %) mine-indexes))]
    (into (sorted-map)
          (map #(vec [%
                      {:state      :covered
                       :mine?      (contains? mine-cells-pos %)
                       :neighbours (init-neighbours-for-cell
                                    %
                                    n-row
                                    n-col
                                    mine-cells-pos)
                       :pos         %}]))
          cells-pos)))

(defn- neighbours
  [{:keys [mine-field]} cell]
  (->  mine-field
       (select-keys (:neighbours cell))
       vals))

(defn initialize-mine-field
  [{:keys [db random-gen-mines]}]
  (let [level-config (-> db
                         :current-level
                         const/config-for-level)
        {:keys [n-col n-row n-mine]} level-config
        cells-data (init-cells-data :n-row n-row
                                    :n-col n-col
                                    :mine-indexes random-gen-mines)]
    {:db (assoc db
                :mine-field                      cells-data
                :ui.game.mf/hit-mine?            false
                :ui.game.mf/non-mine-cells-count (-> (* n-col n-row) (- n-mine))
                :ui.game.mf/revealed-cells-pos   #{})}))

(register-event-fx
 :ui.game.mf/initialize-mine-field
 [(rf/inject-cofx ::random-gen-mines)]
 initialize-mine-field)

(def cell-state-transition-graph
  {:covered       {:investigate        :investigating
                   :toggle-mark        :marked
                   :force-mark         :marked
                   :reveal             :revealed
                   :hit-mine           :exploded}
   :investigating {:cancel-investigate :covered
                   :reveal             :revealed
                   :hit-mine           :exploded}
   :marked        {:toggle-mark        :covered
                   :mark-error         :error-marked}})

(defn new-state
  [old-state action]
  (or (get-in cell-state-transition-graph [old-state action]) old-state))

(defn change-cell-state
  [db {:keys [pos state mine?] :as cell} action]
  (let [new-state* (new-state state action)]
    (cond-> db
      true (assoc-in [:mine-field pos :state] new-state*)

      (and (not mine?)
           (= :revealed new-state*))
      (update :ui.game.mf/revealed-cells-pos conj pos))))

(defn change-cells-state
  [db cells action]
  (reduce (fn [new-db cell]
            (change-cell-state new-db cell action))
          db
          cells))

(defn store-mouse-event
  [db event]
  (assoc db :ui.game/mouse-event event))

(defn clear-mouse-event
 [db]
 (assoc db :ui.game/mouse-event nil))

(defn toggle-investigate-neighbours
  [db {:keys [state] :as cell} {:keys [action with-self?]}]
  (if (#{:investigating :revealed} state)
    (change-cells-state db
                        (if with-self?
                          (conj (neighbours db cell) cell)
                          (neighbours db cell))
                        action)
    db))

(defn investigate-neighbours
  ([db cell]
   (investigate-neighbours db cell {:action :investigate}))
  ([db cell opts]
   (toggle-investigate-neighbours db cell (merge {:action :investigate} opts))))

(defn cancel-investigate-neighbours
  ([db cell]
   (toggle-investigate-neighbours db cell {:action :cancel-investigate}))
  ([db cell opts]
   (toggle-investigate-neighbours db cell (merge {:action :cancel-investigate} opts))))

(declare click-cell)

(defn sweep-or-cancel-investigate-neighbours
  [db {:keys [state] :as cell}]
  (let [neighbours     (neighbours db cell)
        marked-number  (->> neighbours
                            (filter #(= (:state %) :marked))
                            count)
        mine-number    (->> neighbours
                            (filter :mine?)
                            count)]
    (if (and (= :revealed state)
             (= marked-number mine-number))
      (reduce click-cell db neighbours)
      (cancel-investigate-neighbours db cell {:with-self? true}))))

(defn toggle-mark
  [db cell]
  (let [{mine-field :mine-field
         level      :current-level} db
        {state :state}              cell
        total-mines                 (-> (get const/levels level) :n-mine)
        marked-cells                (->> mine-field vals (filter #(= (:state %) :marked)) count)]
    (if-not (and (= marked-cells total-mines)
                 (not= state :marked))
      (change-cell-state db cell :toggle-mark)
      db)))

(defn handle-mouse-event
  [db [current-event-id {:keys [state] :as cell}]]
  (let [previous-event-id (-> db :ui.game/mouse-event first)]
    (case [previous-event-id current-event-id]
      [nil :main-btn-down]
      {:db (-> db
               (store-mouse-event [current-event-id cell])
               (change-cell-state cell :investigate))}

      [nil :aux-btn-down]
      {:db (-> db
               (store-mouse-event [current-event-id cell])
               (toggle-mark cell))}

      [:main-btn-down :leave]
      {:db (change-cell-state db cell :cancel-investigate)}

      [:investigate :leave]
      {:db (cancel-investigate-neighbours db
                                          cell
                                          {:with-self? true})}

      [:main-btn-down :enter]
      {:db (-> db
           (change-cell-state cell :investigate)
           (store-mouse-event [:main-btn-down cell]))}

      [:investigate :enter]
      {:db (-> db
           (store-mouse-event [:investigate cell])
           (investigate-neighbours cell {:with-self? true}))}

      [:main-btn-down :main-btn-up]
      {:db (-> db
               clear-mouse-event
               (click-cell cell))
       :dispatch [:ui.game/update-game-state :should-start? :should-end?]}

      [:aux-btn-down :aux-btn-up]
      {:db (clear-mouse-event db)}

      ([:aux-btn-down :main-btn-down] [:main-btn-down :aux-btn-down])
      {:db (-> db
           (store-mouse-event [:investigate cell])
           (investigate-neighbours cell))}

      ([:investigate :aux-btn-up] [:investigate :main-btn-up])
      {:db (-> db
               clear-mouse-event
               (sweep-or-cancel-investigate-neighbours cell))
       :dispatch [:ui.game/update-game-state :should-end?]}

      ;; Default
      {:db (clear-mouse-event db)})))

(defn- ->mouse-event
  [mouse-event-id {:keys [btn-code cell]}]
  (case [btn-code mouse-event-id]
    [0 :down]    [:main-btn-down cell]
    [2 :down]    [:aux-btn-down cell]
    [0 :up]      [:main-btn-up cell]
    [2 :up]      [:aux-btn-up cell]
    [nil :enter] [:enter cell]
    [nil :leave] [:leave cell]
    [:ignore nil]))

(defn apply-in-allowed-game-states
  [{:keys [game-state] :as db} fn & args]
  (if-not (#{:win :lose} game-state)
    (apply fn (into [db] args))
    {:db db}))

(defn mouse-event-handler
  [{:keys [db]} [_ mouse-event-id {:keys [pos] :as data}]]
  (let [cell        (get-in db [:mine-field pos])
        mouse-event (->mouse-event mouse-event-id (assoc data :cell cell))
        fx          (apply-in-allowed-game-states db handle-mouse-event mouse-event)]
    fx))

(register-event-fx
 :ui.game/mouse-event-handler
 mouse-event-handler)

(defn auto-sweep-neighbours
  ([db cell]
   (let [neighbours (neighbours db cell)]
     #_(print "\n\n#####################################################################\n"
            "#### auto-sweep for: " cell "\n"
            "#####################################################################")
     (if (not-any? #(or (:mine? %)
                        (= (:state %) :marked))
                   neighbours)
       (reduce click-cell db (filter #(= (:state %) :covered) neighbours))
       db))))

(defn click-cell
  [db {:keys [mine? state] :as cell}]
  #_(print "{:click-cell " cell "}")
  (cond
    (and (#{:covered :investigating} state) mine?)
    (-> db
        (change-cell-state cell :hit-mine)
        (assoc :ui.game.mf/hit-mine? true))

    (and (#{:covered :investigating} state) (not mine?))
    (-> db
        (change-cell-state cell :reveal)
        (auto-sweep-neighbours cell))

    :else db))

(defn mark-all-mines
  [db _]
  (change-cells-state db
                      (->> db :mine-field vals (filter :mine?))
                      :force-mark))

(register-event-db
 :ui.game.mf/mark-all-mines
 mark-all-mines)

(defn uncover-all-mines
  [db _]
  (change-cells-state db
                     (->> db :mine-field vals (filter :mine?))
                     :reveal))

(register-event-db
 :ui.game.mf/uncover-all-mines
 uncover-all-mines)

(defn error-marked-cells
  [db _]
  (change-cells-state db
                      (->> db
                           :mine-field
                           vals
                           (filter #(and (= (:state %) :marked)
                                         (-> % :mine? not))))
                      :mark-error))

(register-event-db
 :ui.game.mf/error-marked-cells
 error-marked-cells)

;; -- cofx Registrations -------------------------------------------------------
;;
(defn random-gen-mines*
  "Randomly generate mines which is represeented as a vector
  of n-mine random integers in (range n-cell)"
  [n-cell n-mine]
  (let [xf (comp
            (distinct)
            (take n-mine))]
    (transduce xf conj  (repeatedly #(rand-int n-cell)))))

(defn random-gen-mines
  [{:keys [db] :as cofx} _]
   (let [level-config (-> db :current-level const/config-for-level)
         {:keys [n-col n-row n-mine]} level-config
         mines (random-gen-mines* (* n-row n-col) n-mine)]
     (assoc cofx :random-gen-mines mines)))

(rf/reg-cofx
 ::random-gen-mines
 random-gen-mines)
