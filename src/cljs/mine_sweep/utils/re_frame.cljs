(ns mine-sweep.utils.re-frame
  (:require [re-frame.core :as rf]
            [clojure.spec.alpha :as spec]))

(def validate-spec
  (rf/->interceptor
   :id validate-spec
   :after
   (fn [context]
     (let [new-db (rf/get-effect context :db)
           event  (-> context (rf/get-effect :event) first)]
       (when-not (spec/valid? :mine-sweep.db/db new-db)
         (throw (spec/explain-str :mine-sweep.db/db new-db)))
       context))))

(def default-interceptors
  (when js/goog.DEBUG validate-spec))

(defn register-event-fx
  ([event handler] (register-event-fx event nil handler))
  ([event interceptors handler]
   (rf/reg-event-fx event [default-interceptors interceptors] handler)))

(defn register-event-db
  ([event handler] (register-event-db event nil handler))
  ([event interceptors handler]
   (rf/reg-event-db event [default-interceptors interceptors] handler)))
