(ns mine-sweep.styles
  (:require [mine-sweep.common.styles :as common-styles]
            [mine-sweep.common.constants :as const])
  (:require-macros [cuerdas.core :as gstr]))

(def main
  {:flex             1
   :display          "flex"
   :flex-direction   "column"
   :justify-content  "center"
   :background-color "#00FFFF"
   :background-image "url(img/bg-img.jpeg)"})

(def panel
  (common-styles/with-convex {:box-sizing       "border-box"
                              :padding          "3px 10px 10px 10px"
                              :width            "min-content"
                              :background-color common-styles/grey-bg-color
                              :margin           "0 auto"
                              :display          "flex"
                              :flex-flow        "column"}))

(def toolbar
  {:padding          "8px 0 0"
   :height           "1.2em"
   :margin-bottom    "10px"
   :border-bottom    (gstr/istr "1px solid ~{common-styles/darkgrey-bg-color}")
   :background-color "transparent"})

(def widgets-panel
  (common-styles/with-concave {:box-sizing       "inherit"
                               :display          "flex"
                               :flex-flow        "row"
                               :justify-content  "space-between"
                               :margin-bottom    "10px"
                               :padding          "5px 3px 5px 3px"
                               :background-color common-styles/grey-bg-color}))

(defn mine-field
  [level]
  (let [config        (get const/levels level)
        padding       4
        width         (* const/cell-size (:n-col config))
        height        (* const/cell-size (:n-row config))
        bg-grid-color common-styles/dark-shadow-color]
    (common-styles/with-concave {:display             "flex"
                                 :flex-flow           "row wrap"
                                 :justify-content     "center"
                                 :box-sizing          "padding-box"
                                 :padding             (common-styles/num->px padding)
                                 :margin              "auto"
                                 :background          common-styles/grey-bg-color
                                 :background-image    (gstr/istr "linear-gradient(to right, ~{bg-grid-color} 1px, transparent 0),"
                                                                 "linear-gradient(~{bg-grid-color} 1px, transparent 0),"
                                                                 "linear-gradient(to top, ~{bg-grid-color} 1px, transparent 0),"
                                                                 "linear-gradient(to left, ~{bg-grid-color} 1px, transparent 0)")
                                 :background-size     (gstr/istr "~{(common-styles/num->px-pair const/cell-size)},"
                                                                 "~{(common-styles/num->px-pair const/cell-size)},"
                                                                 "~{(common-styles/num->px-pair width height)},"
                                                                 "~{(common-styles/num->px-pair width height)}")
                                 :background-position (common-styles/num->px-pair padding)
                                 :background-clip     "content-box"
                                 :width               (common-styles/num->px width)
                                 :height              (common-styles/num->px height)})))
