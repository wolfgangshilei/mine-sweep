(ns mine-sweep.ui.game-panel.mine-field.events-test
  (:require [cljs.test :refer-macros [deftest testing is] :refer [report update-current-env!]]
            [cljs.spec.alpha :as s]
            [cljs.spec.test.alpha :as stest]
            [cljs.spec.gen.alpha :as gen]
            [mine-sweep.ui.game-panel.mine-field.events :as events]
            [mine-sweep.db :refer [default-db]]
            [mine-sweep.ui.common.constants :as const]
            [mine-sweep.ui.game-panel.db]))

(s/def ::db :mine-sweep.db/db)
(s/def ::mine-field :ui.game/mine-field)
(s/def ::non-neg-int? (s/and int? (complement neg?)))
(s/def ::pos-int? (s/and int? pos?))
(s/def ::random-gen-mines (s/coll-of ::non-neg-int? :kind vector?))

(s/def ::random-gen-mines*-args (s/and (s/cat :n-cell pos-int? :n-mine pos-int?)
                                       #(> (:n-cell %) (:n-mine %))))
(def random-gen-mines*-args-generator
  #(gen/fmap
    (fn [n] [n (mod n 100)])
    (gen/large-integer* {:min 101 :max 500})))

(s/fdef events/random-gen-mines*
        :args ::random-gen-mines*-args
        :ret  ::random-gen-mines
        :fn   (s/and #(= (-> % :ret count) (-> % :args :n-mine))
                     #(< (->> % :ret (apply max)) (-> % :args :n-cell))
                     #(<= 0 (->> % :ret (apply min)))
                     #(->> % :ret (apply distinct?))))

(deftest random-gen-mines*
  (testing "Test events/random-gen-mines*"
    (let [result
          (stest/check `events/random-gen-mines*
                       {:gen {::random-gen-mines*-args
                              random-gen-mines*-args-generator}})]
      (is (= true (-> result first :clojure.test.check/ret :result))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def ::cell-pos-data map?)
(s/def ::mine-indexes ::random-gen-mines)
(s/def ::init-cells-data-args (s/keys* :req-un [::cells-pos-data ::mine-indexes]))

(def init-cells-data-args-generator
  #(gen/fmap
    (fn [level]
      (let [{:keys [n-row n-col n-mine cells-pos-data]} (get const/levels level)]
        [:cells-pos-data cells-pos-data
         :mine-indexes   (events/random-gen-mines* (* n-row n-col) n-mine)]))
    (s/gen #{:easy :medium :hard})))

(defn check-mines-count
  [{:keys [ret args]}]
  (let [{:keys [mine-indexes]} args]
    (= (->> ret vals (filter :mine?) count)
       (count mine-indexes))))

(defn check-neighbours-count
  "Use a weak condition to check the neighbours are setup properly.
  Enhance it when necessary."
  [{:keys [ret args]}]
  (let [{:keys [mine-indexes]} args]
    (->> ret
         vals
         (map :mine-neighbours-count)
         (every? #(<= 0 % 8)))))

(s/fdef events/init-cells-data
  :args ::init-cells-data-args
  :ret  ::mine-field
  :fn   (s/and check-mines-count
               check-neighbours-count))

(deftest init-cells-data
  (testing "Test events/init-cell-data"
    (let [result (stest/check `events/init-cells-data
                              {:clojure.test.check/opts {:num-tests 100}
                               :gen {::init-cells-data-args
                                     init-cells-data-args-generator}})]
      (is (= true (-> result first :clojure.test.check/ret :result))))))

(deftest click-cell
  (testing "Test events/click-cell"
    (let [{test-db :db} (events/initialize-mine-field {:db default-db
                                                       :random-gen-mines [0 10 20 30 40 50 60 70 80]})
          get-cell (fn [db pos] (get-in db [:mine-field pos]))
          mine-pos [3 3]
          empty-cell [0 8]
          db-mine-clicked (events/click-cell test-db (get-cell test-db mine-pos))
          db-auto-swept   (events/click-cell test-db (get-cell test-db empty-cell))]
      (is (= (-> (get-cell test-db [0 1]) :mine-neighbours-count) 2))
      (is (= (-> db-auto-swept :ui.game.mf/revealed-cells-pos count) 34))
      (is (= (:state (get-cell db-mine-clicked mine-pos))
             :exploded)))))
