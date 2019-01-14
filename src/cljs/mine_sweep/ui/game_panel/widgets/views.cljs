(ns mine-sweep.ui.game-panel.widgets.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [mine-sweep.ui.game-panel.widgets.subs]
            [mine-sweep.ui.game-panel.widgets.styles :as styles]))

(defn timer []
  (let [t (rf/subscribe [:ui.game.widget/timer])]
    [:text {:style styles/timer} @t]))

(defn remaining-mine-counter []
  (let [n-remaining (rf/subscribe [:ui.game.widget/remaining-mines-count])]
    [:text {:style styles/remaining-mine-counter} @n-remaining]))

(defn reset-btn
  []
  (let [bg       (rf/subscribe [:ui.game.widget/reset-btn-background])
        pressed? (r/atom false)]
    (fn []
      [:div {:style          (styles/reset-btn @bg @pressed?)
             :on-mouse-down  #(reset! pressed? true)
             :on-mouse-up    #(reset! pressed? false)
             :on-mouse-leave #(reset! pressed? false)
             :on-click       #(rf/dispatch [:ui.game/restart-game])}])))

(defn widgets-panel
  []
  [:section {:style styles/widgets-panel}
   [remaining-mine-counter]
   [reset-btn]
   [timer]])
