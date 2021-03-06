(ns mine-sweep.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [mine-sweep.db]
   [mine-sweep.events :as events]
   [mine-sweep.views :as views]
   [stylefy.core :as stylefy]))

(defn dev-setup []
  (when goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (stylefy/init)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
