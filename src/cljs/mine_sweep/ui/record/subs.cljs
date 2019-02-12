(ns mine-sweep.ui.record.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :ui.record/my-username
 (fn [db _]
   (-> db :ui.auth/session :username)))

(rf/reg-sub
 :ui.record/records
 (fn [db [_ username]]
   (get-in db [:ui.record/records username])))
