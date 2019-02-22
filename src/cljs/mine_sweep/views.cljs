(ns mine-sweep.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [mine-sweep.subs :as subs]
   [mine-sweep.styles :as styles]
   [mine-sweep.ui.game-panel.views :refer [game-panel]]
   [mine-sweep.ui.auth.views :refer [auth-panel]]
   [mine-sweep.ui.record.views :refer [record-panel]]
   [mine-sweep.js-modules.react-transition-group :refer [css-transition]]))


(defn main-panel []
  (let [display-auth-panel (rf/subscribe [:ui/auth-panel])]
    [:div {:style styles/main-container}
     [:main {:style (styles/main @display-auth-panel)}
      [css-transition styles/record-panel-transition
       [record-panel {:style {:flex 1}}]]
      [css-transition styles/mine-field-transition
       [:div {:style {:flex 2}}
        [game-panel]]]]
     [css-transition (assoc styles/auth-panel-transition :in
                      (if (= :none @display-auth-panel)
                        false
                        true))
      [auth-panel @display-auth-panel]]]))
