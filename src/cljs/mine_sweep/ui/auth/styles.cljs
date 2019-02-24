(ns mine-sweep.ui.auth.styles
  (:require-macros [cuerdas.core :as gstr])
  (:require [stylefy.core :as stylefy]
            [garden.core :as garden]
            [garden.color :as color]))

(def ^private dark-color
  (color/rgba 66 62 62 0.8))

(def ^private light-color
  (color/rgb 255 255 255))

(defn auth-panel
  [disable?]
  {:position         "absolute"
   :width            "30%"
   :border           "2px solid #00FFFF"
   :left             "50%"
   :top              "50%"
   :transform        "translate(-50%, -50%)"
   :border-radius    "5px"
   :padding          "0 .5em 0"
   :background-color dark-color
   :filter           (when disable? "brightness(70%)")
   :color            light-color})

(def panel-header-wrapper
  {:display         "flex"
   :justify-content "space-between"
   :padding         ".7em"})

(def title
  {:margin      0
   :font-size   "1.2em"
   :font-weight "bold"})

(def toggle-link
  {:font-size ".8em"
   :color "currentColor"})

(def error-message
  {:margin-bottom "1em"
   :text-align "center"
   :color "red"
   :font-size ".9em"})

(def form-body
  {:margin-bottom "1em"
   :padding       "0 1em 0"})

(def input-wrapper
  {:margin-bottom "1em"
   :display "flex"
   :flex-direction "column"})

(def form-input-style
  {:background-color    (color/darken dark-color 3)
   :background-image    "linear-gradient(currentColor, currentColor)"
   :background-clip     "border-box"
   :background-origin   "border-box"
   :background-position "bottom left"
   :background-size     "0% 1px"
   :background-repeat   "no-repeat"
   :float               "right"
   :border              :none
   :border-bottom       "2px solid transparent"
   :transition          "background-size .2s linear"
   :color               "currentColor"
   :margin-top          ".5em"
   ::stylefy/mode       {:focus {:background-color (color/darken dark-color 7)
                                 :background-size  "100% 2px"
                                 :outline          :none
                                 :caret-color      light-color}}})

(def form-btns-wrapper
  {:margin         "1em"
   :display        "flex"
   :flex-direction "row-reverse"})

(def cancel-btn
  {:color            (color/darken dark-color 20)
   :border           :none
   :background-color light-color
   :border-radius    ".3em"
   :padding          ".2em 1em"
   ::stylefy/mode    {:active {:background-color (color/darken dark-color 20)
                               :color            light-color}
                      :focus  {:outline :none}}})

(def submit-btn
  {:color            light-color
   :margin-left      "1em"
   :background-color :transparent
   :border           "1px solid"
   :border-color     light-color
   :padding          ".2em 1em"
   :border-radius    ".3em"
   ::stylefy/mode    {:active {:background-color (color/darken dark-color 30)
                               :border-color     :transparent
                               :color            (color/darken light-color 5)}
                      :focus  {:outline :none}
                      :disabled {:background-color (color/darken dark-color 30)
                                 :border-color     :transparent
                                 :color            (color/darken light-color 5)}}})
