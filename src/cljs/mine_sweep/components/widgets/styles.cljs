(ns mine-sweep.components.widgets.styles
  (:require [mine-sweep.common.styles :as common-styles]))

(def ^private widget-text
  {:font-size        "1.5rem"
   :text-align       "center"
   :color            "red"
   :background-color "black"})

(def timer
  (merge widget-text {:width "2.5em"}))

(def remaining-mine-counter
  (merge widget-text {:width "2em"}))

(def reset-btn
  (->> {:font-size        "0.8em"
        :box-sizing       "border-box"
        :width            "1.9rem"
        :height           "1.7rem"
        :padding          "2px"
        :background-color "transparent"
        :align-self       "center"
        :outline          "none"}
       (merge widget-text)
       common-styles/with-convex))
