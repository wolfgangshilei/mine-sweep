(ns mine-sweep.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [mine-sweep.subs :as subs]
   [mine-sweep.styles :as styles]
   [mine-sweep.ui.game-panel.views :refer [game-panel]]
   [mine-sweep.ui.auth.views :refer [auth-panel]]
   [mine-sweep.ui.record.views :refer [record-panel]]
   [stylefy.core :as stylefy]
   [mine-sweep.js-modules.react-transition-group :refer [css-transition]]))

(stylefy/class "fade-appear" {:opacity 0
                              :transform "scale(0.3)"})
(stylefy/class "fade-appear-active" {:opacity 1
                                     :transform "scale(1)"
                                     :transition "all 400ms linear"})

(def transition-opts
  {:class-names "fade"
   :timeout 100
   :appear true
   :in true})

(defn main-panel []
  (let [display-auth-panel (rf/subscribe [:ui/auth-panel])]
    [:div {:style styles/main-container}
     [:main {:style (styles/main @display-auth-panel)}
      [css-transition transition-opts
       [record-panel {:style {:flex 1}}]]
      [css-transition transition-opts
       [:div {:style {:flex 2}}
        [game-panel]]]]
     [auth-panel @display-auth-panel]]))
