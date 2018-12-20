(ns mine-sweep.components.widgets.views
  (:require [mine-sweep.components.widgets.styles :as styles]
            [cuerdas.core :as gstr]))

(defn- padding-0
  [n length]
  (-> n
      (gstr/pad {:length length :padding "0"})))

(defn timer []
  (let [tval 0.5]
    [:text {:style styles/timer} (-> tval str (padding-0 5))]))

(defn remaining-mine-counter []
  (let [n-remaining "15"]
    [:text {:style styles/remaining-mine-counter} (-> n-remaining str (padding-0 3))]))

(defn reset-btn
  [{:keys [style]}]
  (let [btn-state :laugh]
    [:button {:style (merge styles/reset-btn style)}
      "^_^"]))
