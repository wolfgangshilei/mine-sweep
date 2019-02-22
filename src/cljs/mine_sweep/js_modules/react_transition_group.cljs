(ns mine-sweep.js-modules.react-transition-group
  (:require
   [reagent.core :as r]
   ["react-transition-group" :refer [Transition CSSTransition]]))

(def transition (r/adapt-react-class Transition))
(def css-transition (r/adapt-react-class CSSTransition))
