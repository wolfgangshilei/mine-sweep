(ns mine-sweep.ui.record.styles
  (:require-macros [cuerdas.core :as gstr])
  (:require [mine-sweep.js-modules.react-transition-group :as rtg :include-macros true]
            [stylefy.core :as stylefy]))

(def record-panel
  {:margin           "0 7%"
   :min-height       "80%"
   :border-radius    "4px"
   :color            "white"
   :background-color "hsla(0,0%,50%,.5)"})

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

(def rotation-duration 700)

(def rotation
  {:appear {:transform-origin  "left bottom"
            :transform "scale(.1, .1)"}
   :appear-active {:transform  "scale(1, 1)"
                   :transition (gstr/istr "all ~{rotation-duration}ms cubic-bezier(.65, -0.58, .44, 3.85)")
                   :transition-delay "800ms"}})

(rtg/def-transition!
  login-link-transition
  rotation
  {:timeout {:appear 800}
   :appear  true
   :in      true})

(def username-text
  {:font-size     "1.5em"
   :font-weight   "bold"
   :color         "currentColor"
   :display       "block"
   :width         "100%"
   :text-align    "center"
   :border-bottom "1px solid currentColor"
   :margin        ".5em 0"})

(def username
  {:color :crimson})

(def records-order-options
  {:display         "flex"
   :flex-direction  "row"
   :justify-content "space-around"
   :font-size       "1.2em"
   :color           "currentColor"
   :margin-bottom   ".5em"})

(def record-table
  {:border-collapse "collapse"
   :width           "100%"
   :table-layout    "fixed"
   :text-align      "center"})

(def record-table-header
  {:background-color "#CCC"
   :color            "black"
   :font-size        "1.1em"})

(def record-table-col-level
  {:width  "20%"
   :border "1px solid #ddd"})

(def record-table-col-finish-time
  {:width "25%"
   :border "1px solid #ddd"})

(def record-table-col-datetime
  {:text-overflow "ellipsis"
   :border        "1px solid #ddd"})
