(ns mine-sweep.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [mine-sweep.events :as events]
   [mine-sweep.views :as views]
   [mine-sweep.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
