(ns mine-sweep.ui.game-panel.mine-field.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :ui.game.mf/mine-field
 (fn [db _]
   (:ui.game.mf/mine-field db)))

(rf/reg-sub
 :ui.game.mf/cells-pos
 :<- [:ui.game.mf/mine-field]
 (fn [mine-field]
   (keys mine-field)))

(rf/reg-sub
 :ui.game.mf/cell-data
 :<- [:ui.game.mf/mine-field]
 (fn [mine-field [_ pos]]
   (get mine-field pos)))

(rf/reg-sub
 :ui.game.mf/cell-content
 (fn [[_ pos]]
   [(rf/subscribe [:ui.game.mf/cell-data pos])])
 (fn [[cell] _]
   (let [{:keys [state mine? mine-neighbours-count]} cell]
     (case state
       :revealed       (if mine?
                         :mine
                         mine-neighbours-count)
       :marked         :flag
       :wrongly-marked :cross-flag
       :exploded       :red-mine
       :investigating  :investigating
       ;; Defalut state :covered
       state))))
