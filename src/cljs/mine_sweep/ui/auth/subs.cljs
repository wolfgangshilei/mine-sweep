(ns mine-sweep.ui.auth.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::form-submission-status
 (fn [db _]
   (:ui.auth/form-submission-status db)))

(rf/reg-sub
 :ui.auth/disable-auth-panel?
 :<- [::form-submission-status]
 (fn [status]
   (= :submmiting status)))

(rf/reg-sub
 :ui.auth/error-msg
 (fn [db _]
   (:ui.auth/error-msg db)))
