(ns mine-sweep.components.cell.views
  (:require [re-frame.core :as rf]
            [mine-sweep.components.cell.styles :as styles]))

(defn cell
  [pos]
  (let [{:keys [classes content]} {} #_@(rf/subscribe [:cell-view pos])
        ;; _ (println classes content)
        ]
    [:div {:style (styles/cell false :covered "3")
           :on-mouse-down (fn [e] (rf/dispatch [:cell-mouse-event :down
                                                {:btn-code (.-button e) :pos pos}]))
           :on-mouse-up (fn [e]
                          (.stopPropagation e)
                          (rf/dispatch [:cell-mouse-event :up
                                        {:btn-code (.-button e) :pos pos}]))
           :on-mouse-enter (fn [e] (rf/dispatch [:cell-mouse-event :enter {:pos pos}]))
           :on-mouse-leave (fn [e] (rf/dispatch [:cell-mouse-event :leave {:pos pos}]))}
     ]))
