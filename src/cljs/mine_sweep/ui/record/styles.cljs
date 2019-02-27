(ns mine-sweep.ui.record.styles
  (:require-macros [cuerdas.core :as gstr])
  (:require [mine-sweep.js-modules.react-transition-group :as rtg :include-macros true]
            [stylefy.core :as stylefy]
            [garden.color :as color]))

(def record-panel-bg-color
  (color/hsla 0 0 0.5 0.5))

(def record-panel
  {:margin           "0 7%"
   :min-height       "80%"
   :border-radius    "4px"
   :color            "white"
   :background-color record-panel-bg-color})

(def flex-layout
  {:display         "flex"
   :flex-direction  "column"
   :justify-content "center"
   :align-items     "center"})

(def normal-layout
  {:padding ".5em 1em"})

(def login-text
  {:margin    "3em auto"
   :font-size "1.1rem"})

(def login-link
  {:position        "relative"
   :left            "-2.5em"
   :bottom          "-1em"
   :color           "blue"
   :font-size       "1.4rem"
   :font-weight     "bold"
   :text-decoration "underline"
   :margin          "auto"
   ::stylefy/mode   {:hover {:cursor "pointer"}}})

(def scale-duration 500)

(def scale
  {:appear {:transform-origin  "left bottom"
            :transform "scale(.1, .1)"}
   :appear-active {:transform  "scale(1, 1)"
                   :transition (gstr/istr "all ~{scale-duration}ms cubic-bezier(.65, -0.58, .44, 3.85)")}})

(rtg/def-transition!
  login-link-transition
  scale
  {:timeout {:appear scale-duration}
   :appear  true
   :in      true})

(def username
  {:color :crimson})

(def records-order-options
  {:display         "flex"
   :flex-direction  "row"
   :justify-content "space-around"
   :font-size       "1.2em"
   :color           "currentColor"
   :margin-bottom   ".5em"})
