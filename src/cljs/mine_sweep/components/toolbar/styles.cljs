(ns mine-sweep.components.toolbar.styles
  (:require [cuerdas.core :as gstr]
            [mine-sweep.common.styles :as common-styles]))

(def toolbar-item-container
  {:width          "4em"
   :display        "flex"
   :flex-direction "column"})

(def toolbar-title
  {:outline          "none"
   :padding          ".2em"
   :line-height      1
   :font-weight      "bold"
   :border           (gstr/istr "1px solid ~{common-styles/darkgrey-bg-color}")
   :border-left      "0"
   :border-top       "0"
   :background-color common-styles/grey-bg-color})

(def toolbar-item
  {:outline          "none"
   :padding          ".2em"
   :text-align       "left"
   :line-height      1
   :z-index          1
   :border           (gstr/istr "0 1px 1px solid ~{common-styles/darkgrey-bg-color}")
   :background-color common-styles/lightgrey-bg-color})
