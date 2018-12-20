(ns mine-sweep.views
  (:require
   [re-frame.core :as re-frame]
   [mine-sweep.subs :as subs]
   [mine-sweep.styles :as styles]
   [mine-sweep.ui.game-panel.views :refer [game-panel]]
   ))

(defn widgets []
  [:div
   [:text "widgets"]])

(defn main-panel []
  [:main {:style styles/main}
   [game-panel]])
