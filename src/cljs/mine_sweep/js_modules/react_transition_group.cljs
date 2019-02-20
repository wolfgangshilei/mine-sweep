(ns mine-sweep.js-modules.react-transition-group
  (:require
   [reagent.core :as r]
   ["react-transition-group" :refer [Transition CSSTransition]]))

(def transition (r/adapt-react-class Transition))
(def css-transition (r/adapt-react-class CSSTransition))

#_(defn transition
  [{:keys [default-styles transition-styles] :as opts} component]
  (if (map? (second component))
    (let [comp-style (-> component second :style)]
      [ReactTransition (dissoc opts :default-styles :transition-styles)
       #(r/as-component (update component 1 merge {:style (merge
                                                           comp-style
                                                           default-styles
                                                           (get transition-styles (keyword %)))}))])
    (let [comp-name (first component)
          children  (next component)]
      [ReactTransition (dissoc opts :default-styles :transition-styles)
       #(r/as-component (into [comp-name {:style (merge default-styles
                                                        (get transition-styles (keyword %)))}]
                              children))])))
