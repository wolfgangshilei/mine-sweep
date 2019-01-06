(ns mine-sweep.styles
  (:require [mine-sweep.ui.common.styles :as common-styles]
            [mine-sweep.ui.common.constants :as const])
  (:require-macros [cuerdas.core :as gstr]))

(def main
  {:flex             1
   :display          "flex"
   :flex-direction   "column"
   :justify-content  "center"
   :background-color "#00FFFF"
   :background       "url(img/bg-img.jpeg) center / cover"})
