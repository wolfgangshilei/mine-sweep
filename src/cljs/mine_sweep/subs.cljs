(ns mine-sweep.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 :ui/auth-panel
 (fn [db]
   (:ui.auth/panel db)))
