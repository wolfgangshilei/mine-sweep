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
 :ui.game.mf/mine-neighbours-count
 (fn [[_ pos]]
   [(rf/subscribe [:ui.game.mf/cell-data pos])
    (rf/subscribe [:ui.game.mf/mine-field])])
 (fn [[cell-data mine-field] _]
   (->> cell-data
        :neighbours
        (select-keys mine-field)
        vals
        (filter :mine?)
        count)))

(rf/reg-sub
 :ui.game.mf/cell-content
 (fn [[_ pos]]
   [(rf/subscribe [:ui.game.mf/cell-data pos])
    (rf/subscribe [:ui.game.mf/game-state])
    (rf/subscribe [:ui.game.mf/mine-neighbours-count pos])])
 (fn [[cell game-state mine-neighbours-count] _]
   (let [{:keys [state mine?]} cell]
     (cond
       (and (= state :revealed)
            ((complement zero?) mine-neighbours-count)
            (not mine?))
       mine-neighbours-count

       (and (= game-state :lose)
            (not mine?)
            (= state :marked))
       :error-marked

       :else
       nil))))
