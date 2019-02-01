(ns mine-sweep.styles
  (:require [mine-sweep.ui.common.styles :as common-styles]
            [mine-sweep.ui.common.constants :as const])
  (:require-macros [cuerdas.core :as gstr]))

(def main-container
  {:flex             1
   :display          "flex"})

(defn main
  [auth-panel]
  {:display          "flex"
   :flex             1
   :flex-direction   "column"
   :justify-content  "center"
   :background-color "#00FFFF"
   :background       "url(img/bg-img.jpeg) center / cover"
   :filter           (when-not (= :none auth-panel) "blur(4px)")})
