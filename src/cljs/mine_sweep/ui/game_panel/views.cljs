(ns mine-sweep.ui.game-panel.views
  (:require [re-frame.core :as rf]
            [mine-sweep.ui.game-panel.mine-field.views :refer [mine-field]]
            [mine-sweep.ui.game-panel.widgets.views :refer [widgets-panel]]
            [mine-sweep.ui.game-panel.toolbar.views :refer [toolbar]]
            [mine-sweep.ui.game-panel.styles :as styles]
            [mine-sweep.ui.common.constants :as const]))

(defn game-panel []
  [:div {:style styles/panel
         :on-context-menu (fn [e] (.preventDefault e))}
   [toolbar]
   [widgets-panel]
   [mine-field {:mouse-event-handler (fn [event-id event-data]
                                       (rf/dispatch [:ui.game/mouse-event-handler event-id event-data]))}]])
