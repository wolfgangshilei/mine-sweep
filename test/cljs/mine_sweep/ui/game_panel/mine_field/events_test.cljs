(ns mine-sweep.ui.game-panel.mine-field.events-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [mine-sweep.ui.game-panel.mine-field.events :as events]
            [mine-sweep.db :refer [default-db]]))

(deftest random-gen-mines*
  (testing "Test events/random-gen-mines*"
    (is (= (-> (events/random-gen-mines* 480 99) count) 99))
    (is (-> (events/random-gen-mines* 480 99) distinct?))))

(deftest click-cell
  (testing "Test events/click-cell"
    (let [{test-db :db} (events/initialize-mine-field {:db default-db
                                                       :random-gen-mines [0 10 20 30 40 50 60 70 80]})
          get-cell (fn [db pos] (get-in db [:ui.game.mf/mine-field pos]))
          mine-pos [3 3]
          empty-cell [0 8]
          db-mine-clicked (events/click-cell test-db (get-cell test-db mine-pos))
          db-auto-swept   (events/click-cell test-db (get-cell test-db empty-cell))]
      (is (= (-> (get-cell test-db [0 1]) :mine-neighbours-count) 2))
      (is (= (-> db-auto-swept :ui.game.mf/revealed-cells-pos count) 34))
      (is (= (:state (get-cell db-mine-clicked mine-pos))
             :exploded)))))
