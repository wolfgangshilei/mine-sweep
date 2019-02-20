(ns mine-sweep.ui.game-panel.toolbar.styles
  (:require [cuerdas.core :as gstr]
            [mine-sweep.ui.common.styles :as common-styles]))

(def dropdown-menu
  {:max-width      "4em"
   :display        "flex"
   :flex-direction "column"})

(def drop-menu-selected-background
  "linear-gradient(135deg,aqua,palegreen)")

(defn dropdown-menu-title
  [expanded?]
  (merge {:outline      "none"
          :padding      ".2em"
          :line-height  1
          :border       (gstr/istr "1px solid ~{common-styles/darkgrey-bg-color}")
          :border-left  "0"
          :border-top   "0"}
         (if expanded?
           {:background  drop-menu-selected-background
            :font-weight "bold"}
           {:background common-styles/grey-bg-color})))

(def dropdown-menu-text
  {:white-space   "nowrap"
   :overflow      "hidden"
   :margin        0
   :text-overflow "ellipsis"})

(defn dropdown-menu-item
  [show?]
  (merge  {:position    "relative"
           :z-index     1
           :outline     "none"
           :font-size   ".7em"
           :padding     ".2em"
           :margin      0
           :text-align  "right"
           :line-height 1
           :border      (gstr/istr "0 1px 1px solid ~{common-styles/darkgrey-bg-color}")
           :background  drop-menu-selected-background}
          dropdown-menu-text
          (when-not show? {:display "none"})))

(def dropdown-menu-item-selected-mark
  {:float "left"})

(def toolbar
  (-> {:padding          "8px 0 0"
       :height           "1.2em"
       :margin-bottom    "10px"
       :border-bottom    (gstr/istr "1px solid ~{common-styles/darkgrey-bg-color}")
       :background-color "transparent"
       :cursor           "default"}
      (common-styles/assoc-vendor-prefixed :user-select "none")))
