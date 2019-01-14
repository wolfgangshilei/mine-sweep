(ns mine-sweep.ui.game-panel.widgets.styles
  (:require [cuerdas.core :as gstr]
            [mine-sweep.ui.resources.svg :as svg]
            [mine-sweep.ui.common.styles :as common-styles]))

(def widgets-panel
  (common-styles/with-concave {:box-sizing       "inherit"
                               :display          "flex"
                               :flex-flow        "row"
                               :justify-content  "space-between"
                               :margin-bottom    "10px"
                               :padding          "5px 3px 5px 3px"
                               :background-color common-styles/grey-bg-color}))

(def ^private widget-text
  (-> {:font-size        "1.5rem"
       :text-align       "center"
       :color            "red"
       :background-color "black"
       :cursor           "default"}
      (common-styles/assoc-vendor-prefixed :user-select "none")))

(def timer
  (merge widget-text {:width "2.5em"}))

(def remaining-mine-counter
  (merge widget-text {:width "2.5em"}))

(defn background-image
  [bg]
  (case bg
    :smile   (common-styles/inline-svg svg/smile {:width "70%" :height "70%"})
    :working (common-styles/inline-svg svg/working {:width "70%" :height "70%"})
    :faint   (common-styles/inline-svg svg/faint {:width "70%" :height "70%"})
    :cool    (common-styles/inline-svg svg/cool {:width "70%" :height "70%"})))

(defn reset-btn
  [bg pressed?]
  (let [basic-style
        {:font-size        "0.8em"
         :box-sizing       "border-box"
         :width            "1.7rem"
         :height           "1.7rem"
         :padding          "2px"
         :align-self       "center"
         :outline          "none"
         :background-color "transparent"
         :background       (background-image bg)}]
    (if pressed?
      (common-styles/with-concave basic-style)
      (common-styles/with-convex basic-style))))
