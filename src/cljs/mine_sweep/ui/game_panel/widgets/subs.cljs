(ns mine-sweep.ui.game-panel.widgets.subs
  (:require [re-frame.core :as rf]
            [cuerdas.core :as gstr]
            [mine-sweep.ui.common.constants :as const]))

(defn- padding-0
  [n length]
  (-> n
      (gstr/pad {:length length :padding "0"})))

(rf/reg-sub
 ::total-mines-count
 (fn [db _]
   (->> db :current-level (get const/levels) :n-mine)))

(rf/reg-sub
 :ui.game.widget/remaining-mines-count
 :<- [::total-mines-count]
 :<- [:ui.game.mf/mine-field]
 (fn [[total-mines-count mine-field] _]
   (let [marked-count (->> mine-field vals (filter #(= (:state %) :marked)) count)]
     (-> (- total-mines-count marked-count)
         str
         (padding-0 3)))))

(rf/reg-sub
 :ui.game.widget/reset-btn-background
 (fn [db _]
   (let [{game-state :game-state mouse-event :ui.game/mouse-event} db]
     (cond
       (= game-state :win)   :cool
       (= game-state :lose)  :faint
       (#{:investigate
          :main-btn-down
          :aux-btn-down}
        (first mouse-event)) :working
       :else                 :smile))))

(defn time-str
  [num]
  (-> num
      str
      (padding-0 4)
      (clojure.string/replace #"^([0-9]{3})([0-9])$" "$1.$2")))

(rf/reg-sub
 :ui.game.widget/timer
 (fn [{:ui.game/keys [timer]}]
   (time-str timer)))
