(ns mine-sweep.views
  (:require
   [re-frame.core :as rf]
   [mine-sweep.subs :as subs]
   [mine-sweep.styles :as styles]
   [mine-sweep.ui.game-panel.views :refer [game-panel]]
   [mine-sweep.ui.auth.views :refer [auth-panel]]))

(defn main-panel []
  (let [display-auth-panel (rf/subscribe [:ui/auth-panel])]
    [:div {:style styles/main-container}
     [:main {:style (styles/main @display-auth-panel)}
      (when goog.DEBUG [:button {:on-click #(rf/dispatch [:ui.auth/toggle-panel :login])}])
      [game-panel]]
     [auth-panel @display-auth-panel]]))
