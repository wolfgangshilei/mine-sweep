(ns mine-sweep.ui.game-panel.mine-field.views
  (:require [re-frame.core :as rf]
            [mine-sweep.subs]
            [mine-sweep.ui.game-panel.mine-field.subs]
            [mine-sweep.ui.game-panel.mine-field.styles :as styles]
            [mine-sweep.ui.common.constants :as const]
            [mine-sweep.utils.log :refer [log]]))

(defn- cell-view
  [{:keys [mouse-event-handler cell-data cell-content]}]
  (let [pos (:pos cell-data)]
    [:div {:style (styles/cell cell-data cell-content)
           :on-mouse-down (fn [e]
                            (mouse-event-handler :down {:btn-code (.-button e) :pos pos}))
           :on-mouse-up (fn [e]
                          #_(.stopPropagation e)
                          (mouse-event-handler :up {:btn-code (.-button e) :pos pos}))
           :on-mouse-enter (fn [_]
                             (mouse-event-handler :enter {:pos pos}))
           :on-mouse-leave (fn [e]
                             (mouse-event-handler :leave {:pos pos}))}
     (when (number? cell-content) cell-content)]))

(defn mine-field
  [{:keys [mouse-event-handler]}]
  (let [cells-pos     (rf/subscribe [:ui.game.mf/cells-pos])
        current-level (rf/subscribe [:current-level])
        level-cfg     (get const/levels @current-level)]
    (into [:section {:style (styles/mine-field level-cfg)
                     :on-mouse-leave (fn [e]
                                       (mouse-event-handler :leave-mine-field))}]
          (map #(vector cell-view {:cell-data           @(rf/subscribe [:ui.game.mf/cell-data %])
                                   :cell-content        @(rf/subscribe [:ui.game.mf/cell-content %])
                                   :mouse-event-handler mouse-event-handler}) @cells-pos))))
