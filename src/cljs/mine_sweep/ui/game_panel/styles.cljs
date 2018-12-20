(ns mine-sweep.ui.game-panel.styles
  (:require [mine-sweep.ui.common.styles :as common-styles]))

(def panel
  (common-styles/with-convex {:box-sizing       "border-box"
                              :padding          "3px 10px 10px 10px"
                              :width            "min-content"
                              :background-color common-styles/grey-bg-color
                              :margin           "0 auto"
                              :display          "flex"
                              :flex-flow        "column"}))
