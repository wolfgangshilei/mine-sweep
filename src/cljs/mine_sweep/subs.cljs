(ns mine-sweep.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 :current-level
 (fn [db]
   (:current-level db)))
