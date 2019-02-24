(ns mine-sweep.styles
  (:require [mine-sweep.ui.common.styles :as common-styles]
            [mine-sweep.ui.common.constants :as const]
            [stylefy.core :as stylefy]
            [mine-sweep.js-modules.react-transition-group :as rtg :include-macros true])
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

(def fade-in-duration 400)
(def fade-in-out-duration
  {:enter 200
   :exit  150})

(def fade-in
  {:appear        {:opacity 0}
   :appear-active {:opacity 1
                   :transition (gstr/istr "all ~{fade-in-duration}ms ease-out")}})

(rtg/def-transition!
  mine-field-transition
  fade-in
  {:timeout {:appear fade-in-duration}
   :appear  true
   :in      true})

(def fade-in-out
  {:enter        {:opacity    0}
   :enter-active {:opacity    1
                  :transition (gstr/istr "all ~{(:enter fade-in-out-duration)}ms ease-in")}
   :enter-done   {}
   :exit         {:opacity    1}
   :exit-active  {:opacity    0
                  :transition (gstr/istr "all ~{(:exit fade-in-out-duration)}ms linear")}
   :exit-done    {:opacity    0}})

(rtg/def-transition!
  auth-panel-transition
  fade-in-out
  {:timeout fade-in-out-duration
   :unmount-on-exit true})

(def drop-in
  {:appear        {:transform  "translate(0%, -150%)"}
   :appear-active {:transform  "translate(0%, 0%)"
                   :transition (gstr/istr "all ~{fade-in-duration}ms ease-out")
                   :transition-delay (gstr/istr "~{fade-in-duration}ms")}})

(rtg/def-transition!
  record-panel-transition
  drop-in
  {:timeout {:appear fade-in-duration}
   :appear  true
   :in      true})
