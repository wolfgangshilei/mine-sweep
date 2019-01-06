(ns mine-sweep.ui.game-panel.mine-field.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :ui.game.mf/game-state
 (fn [db _]
   (:game-state db)))

(rf/reg-sub
 :ui.game.mf/mine-field
 (fn [db _]
   (:mine-field db)))

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
   [(rf/subscribe [:ui.game.mf/cell-data pos])
    (rf/subscribe [:ui.game.mf/game-state])])
 (fn [[cell game-state mine-neighbours-count] _]
   (let [{:keys [state mine? mine-neighbours-count]} cell]
     (case [state mine?]
       [:revealed true]  [:revealed :mine]

       [:revealed false] [:revealed mine-neighbours-count]

       [:marked true] [:marked :flag]

       [:marked false] [:marked (if (= game-state :lose) :cross-flag :flag)]

       ;;Defalut
       [state nil]))))
