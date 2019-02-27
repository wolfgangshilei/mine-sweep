(ns mine-sweep.ui.record.tabs.styles
  (:require [stylefy.core :as stylefy]
            [garden.color :as color]))

(def tab-btn-color
  (color/rgb 72 99 112))

(def tab-btn
  {:flex             1
   :padding          ".3em 0"
   :user-select      "none"
   :color            (color/rgb 196 209 219)
   :background-color (-> tab-btn-color
                         (color/darken 15))
   ::stylefy/mode    {:hover {:cursor "pointer"}}})

(def tab-btn-selected
  {:flex             1
   :padding          ".3em 0"
   :user-select      "none"
   :background-color tab-btn-color
   ::stylefy/mode    {:hover {:cursor "pointer"}}})
