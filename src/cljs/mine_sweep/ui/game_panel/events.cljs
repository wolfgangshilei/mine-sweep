(ns mine-sweep.ui.game-panel.events
  (:require [re-frame.core :as rf]
            [mine-sweep.utils.re-frame :refer [register-event-fx register-event-db]]))

(defn restart-game
  [{:keys [db]}]
  {:db          db
   ::stop-timer nil
   :dispatch-n  [[::reset-timer]
                 [:ui.game/update-game-state ::reset]
                 [:ui.game.mf/initialize-mine-field]]})

(register-event-fx
 :ui.game/restart-game
 (fn [cofx _]
   (restart-game cofx)))

(defn set-level
  [cofx [_ level]]
  (-> cofx
      restart-game
      (assoc-in [:db :current-level] level)))

(register-event-fx
 :ui.game/set-level
 set-level)

(register-event-db
 ::tick
 (fn [db _]
   (update db :ui.game/timer #(-> %
                                inc
                                (min 9999)))))

(register-event-db
 ::reset-timer
 (fn [db _]
   (assoc db :ui.game/timer 0)))

(defn- tick
  []
  (rf/dispatch [::tick]))

(defonce ^:dynamic *timer* nil)

(rf/reg-fx
 ::start-timer
 (fn [_]
   (when (not *timer*)
     (let [t (js/setInterval tick 100)]
       (tick)
       (set! *timer* t)))))

(rf/reg-fx
 ::stop-timer
 (fn [_]
   (when *timer*
     (js/clearInterval *timer*)
     (set! *timer* nil))))

(defn update-game-state
  [{:keys [db] :as fx} state]
  (case state
    ::reset
    (-> fx
        (assoc-in [:db :game-state] :reset))

    :should-start?
    (if (= (:game-state db)
           :reset)
      (-> fx
          (assoc-in [:db :game-state] :ongoing)
          (assoc ::start-timer nil))
      fx)

    :should-end?
    (let [lose?      (:ui.game.mf/hit-mine? db)
          win?       (= (:ui.game.mf/non-mine-cells-count db)
                        (-> db :ui.game.mf/revealed-cells-pos count))]
      (cond
        lose? (-> fx
                  (assoc-in [:db :game-state] :lose)
                  (assoc ::stop-timer nil
                         :dispatch    [:ui.game.mf/uncover-all-mines]))
        win?  (-> fx
                  (assoc-in [:db :game-state] :win)
                  (assoc ::stop-timer nil
                         :dispatch    [:ui.game.mf/mark-all-mines]))
        :else fx))

    ;; Default
    fx))

(register-event-fx
 :ui.game/update-game-state
 (fn [{:keys [db]} [_ & states]]
   (reduce (fn [fx state]
             (update-game-state fx state))
           {:db db}
           states)))
