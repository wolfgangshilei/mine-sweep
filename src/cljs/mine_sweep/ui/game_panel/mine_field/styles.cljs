(ns mine-sweep.ui.game-panel.mine-field.styles
  (:require [cuerdas.core :as gstr]
            [mine-sweep.ui.resources.svg :as svg]
            [mine-sweep.ui.common.styles :as common-styles]
            [mine-sweep.ui.common.constants :as const]))

(defn mine-field
  [{:keys [n-col n-row]}]
  (let [padding       4
        width         (* const/cell-size n-col)
        height        (* const/cell-size n-row)
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

(defn- cell-basic-styles
  [styles]
  (into {:height (common-styles/num->px const/cell-size)
         :width  (common-styles/num->px const/cell-size)}
        styles))

(def number-cell-style
  (let [numbered-color
        {0 "black"
         1 "#0720F4"
         2 "#007B23"
         3 "#FF0017"
         4 "#010976"
         5 "#7E0006"
         6 "#0C7E81"
         7 "#000000"
         8 "#808080"}]
    (map (fn [[num color]]
           [num (-> {:color            color
                     :text-align       "center"
                     :vertical-align   "center"
                     :cursor           "default"
                     :background-color "transparent"}
                    (common-styles/assoc-vendor-prefixed :user-select "none")
                    (cell-basic-styles))])
         numbered-color)))

(def cell-styles
  (into {:investigating (cell-basic-styles {:background-color "transparent"})
         :red-mine      (cell-basic-styles
                         {:background (gstr/istr "~{(common-styles/inline-svg svg/mine)},red")})
         :flag          (common-styles/with-convex
                          (cell-basic-styles
                           {:background-color "transparent"
                            :background       (common-styles/inline-svg svg/flag)}))
         :mine          (cell-basic-styles
                         {:background-color "transparent"
                          :background (common-styles/inline-svg svg/mine)})
         :cross-flag    (cell-basic-styles
                         {:background (gstr/istr "linear-gradient(45deg, transparent 47%, red 0, red 53%, transparent 0),"
                                                 "linear-gradient(-45deg, transparent 47%, red 0, red 53%, transparent 0),"
                                                 "~{(common-styles/inline-svg svg/flag)}")})

         :covered       (common-styles/with-convex (cell-basic-styles
                                                    {:background-color common-styles/darkgrey-bg-color}))}
        number-cell-style))

(defn cell
  [content]
  (get cell-styles content))
