(ns mine-sweep.ui.common.styles
  (:require-macros [cuerdas.core :as gstr]))

(defn- grey-rgb
  [value]
  (gstr/istr "rgb(~{value},~{value},~{value})"))

(def ^private shadow-width "2px")
(def ^private shadow-blur  "2px")
(def light-shadow-color (grey-rgb 255))
(def dark-shadow-color (grey-rgb 123))

(def grey-bg-color (grey-rgb 207))
(def lightgrey-bg-color (grey-rgb 240))
(def darkgrey-bg-color (grey-rgb 189))

(defn num->px [num]
  (str num "px"))

(defn num->px-pair
  ([num]
   (clojure.string/join " " (map num->px (repeat 2 num))))
  ([num1 num2]
   (clojure.string/join " " (map num->px [num1 num2]))))

(defn with-concave
  [styles]
  (merge styles
         #_{:box-sizing "border-box"
          :border-top (gstr/istr "~{shadow-width} solid ~{dark-shadow-color}")
          :border-left (gstr/istr "~{shadow-width} solid ~{dark-shadow-color}")
          :border-right (gstr/istr "~{shadow-width} solid ~{light-shadow-color}")
          :border-bottom (gstr/istr "~{shadow-width} solid ~{light-shadow-color}")}

         {:box-shadow (clojure.string/join ","
                                [(gstr/istr "inset -~{shadow-width} -~{shadow-width} ~{shadow-blur} ~{light-shadow-color}")
                                 (gstr/istr "inset ~{shadow-width} ~{shadow-width} ~{shadow-blur} ~{dark-shadow-color}")])}))

(defn with-convex
  [styles]
  (merge styles
         #_{:box-sizing "border-box"
          :border-top (gstr/istr "~{shadow-width} solid ~{light-shadow-color}")
          :border-left (gstr/istr "~{shadow-width} solid ~{light-shadow-color}")
          :border-right (gstr/istr "~{shadow-width} solid ~{dark-shadow-color}")
          :border-bottom (gstr/istr "~{shadow-width} solid ~{dark-shadow-color}")}

         {:box-shadow (clojure.string/join ","
                                [(gstr/istr "inset -~{shadow-width} -~{shadow-width} ~{shadow-blur} ~{dark-shadow-color}")
                                 (gstr/istr "inset ~{shadow-width} ~{shadow-width} ~{shadow-blur} ~{light-shadow-color}")])}))

(defn inline-svg
  ([svg]
   (gstr/istr "url('data:image/svg+xml,~{svg}') center / 100% 100% no-repeat"))
  ([svg {:keys [width height]}]
   (gstr/istr "url('data:image/svg+xml,~{svg}') center / ~{width} ~{height} no-repeat")))
