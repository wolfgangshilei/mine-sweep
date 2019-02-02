(ns mine-sweep.ui.game-panel.mine-field.views
  (:require [re-frame.core :as rf]
            [mine-sweep.subs]
            [mine-sweep.ui.game-panel.mine-field.subs]
            [mine-sweep.ui.game-panel.mine-field.styles :as styles]
            [mine-sweep.ui.common.constants :as const]
            [mine-sweep.utils.log :refer [log]]))

(defn- cell-view
  [{:keys [mouse-event-handler pos cell-content]}]
  [:div {:style (styles/cell cell-content)
         :on-mouse-down (fn [e]
                          (mouse-event-handler :down {:btn-code (.-button e) :pos pos}))
         :on-mouse-up (fn [e]
                        ;; Prevent the event from bubbling up to the mine field's mouse-up
                        ;; event handler once it has been handled by the cell's handler.
                        (.stopPropagation e)
                        (mouse-event-handler :up {:btn-code (.-button e) :pos pos}))
         :on-mouse-enter (fn [_]
                           (mouse-event-handler :enter {:pos pos}))
         :on-mouse-leave (fn [e]
                           (mouse-event-handler :leave {:pos pos}))}
   (when (pos? cell-content) cell-content)])

(defn mine-field
  [{:keys [mouse-event-handler]}]
  (let [cells-pos     (rf/subscribe [:ui.game.mf/cells-pos])
        current-level (rf/subscribe [:current-level])
        level-cfg     (get const/levels @current-level)]
    (into [:div {:style          (styles/mine-field level-cfg)
                 ;; The mouse's buttons (particularly the main button could be released
                 ;; at the padding zone of the mine field. Such an event will not trigger
                 ;; the event handlers registered on the cells and can result in corrupted state
                 ;; transitions. It thus needs to be handled here separately.
                 :on-mouse-up    (fn [e]
                                   (rf/dispatch [:ui.game.mf/clear-mouse-event]))
                 :on-mouse-leave (fn [e]
                                   (mouse-event-handler :leave-mine-field))}]
          (map #(vector cell-view {:pos                 %
                                   :cell-content        @(rf/subscribe [:ui.game.mf/cell-content %])
                                   :mouse-event-handler mouse-event-handler}) @cells-pos))))
