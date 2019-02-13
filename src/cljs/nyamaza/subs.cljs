(ns nyamaza.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::post-text
 (fn [db _]
   (:post-text db)))

(re-frame/reg-sub
 :user
 (fn [db _]
   (:user db)))

(re-frame/reg-sub
 ::post-successful
 (fn [db _]
   (:is-post-successful? db)))

(re-frame/reg-sub
 ::encryption-selection
 (fn [db _]
   (:set-encryption db)))

(re-frame/reg-sub
 ::phone-number
 (fn [db _]
   (:phone-number db)))

(re-frame/reg-sub
 ::secret-message
 (fn [db _]
   (:secret-message db)))

(re-frame/reg-sub
 ::message
 (fn [db _]
   (:message db)))
