(ns mine-sweep.views
  (:require
   [re-frame.core :as re-frame]
   [mine-sweep.subs :as subs]
   [mine-sweep.styles :as styles]
   [mine-sweep.components.cell.views :as cell-view]
   [mine-sweep.components.widgets.views :as widgets]
   [mine-sweep.components.toolbar.views :as toolbar]
   [mine-sweep.common.constants :as const]
   ))

(defn widgets []
  [:div
   [:text "widgets"]])

(defn main-panel []
  (let [name          (re-frame/subscribe [::subs/name])
        current-level :hard
        level-cfg     (get const/levels current-level)
        total-cells   (* (:n-row level-cfg) (:n-col level-cfg))]
    [:main {:style styles/main}
     [:section {:style styles/panel}
      [:section {:style styles/toolbar}
       [toolbar/toolbar-item
        (toolbar/select-level current-level)]]
      [:section {:style styles/widgets-panel}
       [widgets/remaining-mine-counter]
       [widgets/reset-btn {:style {:position "relative"
                                   :left     "0.3em"}}]
       [widgets/timer]]
      (into [:section {:style (styles/mine-field current-level)}]
            (repeat total-cells (cell-view/cell 1)))]]))
