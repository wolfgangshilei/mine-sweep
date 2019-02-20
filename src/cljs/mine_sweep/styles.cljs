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
   :flex-direction   "row"
   :justify-content  "center"
   :align-items      "center"
   :background       "url(img/bg-img.jpeg) center / cover, linear-gradient(rgba(0,143,228,.5), rgba(196,137,123,.5))"
   :filter           (when-not (= :none auth-panel) "blur(4px)")})
