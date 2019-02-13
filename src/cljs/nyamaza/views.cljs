(ns nyamaza.views
  (:require
   [re-frame.core :as re-frame]
   [nyamaza.subs :as subs]
   [nyamaza.events :as events]
   [cljsjs.noty]
   [clojure.string :as string]
   ))


(defn noty
  [config]
  (.show (js/Noty. (clj->js config))))

(def successful-post
  {:text "post successful"
   :layout "bottomLeft"
   :timeout "3000"
   :type "success"
   :theme "semanticui"
   })


(defn tab-bar
  []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div.tabs.is-centered
     [:ul

      [:li {:class (when (= @active-panel :encrypt-panel)
                     "is-active")}
       [:a.is-size-4 {:href "#/encrypt"}
        [:span.icon.is-small [:i.fas.fa-lock]]
        [:span "encrypt"]]]

      [:li {:class (when (= @active-panel :decrypt-panel)
                     "is-active")}
       [:a.is-size-4 {:href "#/decrypt"}
        [:span.icon.is-small [:i.fas.fa-lock-open]]
        [:span "decrypt"]]]]]))


(defn text-input
  [label placeholder]
  [:div.field
   [:label.label label]
   [:div.control
    [:input.input {:type "text"
                   :placeholder placeholder
                   :on-change #(re-frame/dispatch [::events/text
                                                   (-> %
                                                       .-target
                                                       .-value)])}]]]
  )

(defn text-area
  [message placeholder]
  [:div.field.is-horizontal
   [:div.field-label.is-normal
    [:label.label message]]
   [:div.field-body
    [:div.field
     [:div.control
      [:textarea.textarea {:placeholder placeholder}]]]]]

  )

(defn select-encryption
  []
  [:div.field
   [:label.label "Select Encryption"]
   [:div.control
    [:div.select
     [:select
      [:option "AES"]
      [:option "DES"]]]]])


(defn button
  [value]
  [:input.button.is-rounded
   {:value value
    :type "button"
    :on-click #(js/console.log "pressed!!!")}])

(defn cs [& names]
  (string/join " " (filter identity names)))

(defn pick-encryption
  []
  (let [encryption-selection (re-frame/subscribe [::subs/encryption-selection])]
    [:div.buttons
     [:input {:class (cs "button is-rounded"
                         (when (= :aes @encryption-selection) "is-success"))
              :value "AES"
              :type "button"
              :on-click #(re-frame/dispatch [::events/select-encryption
                                             :aes])}]

     [:input {:class (cs "button is-rounded"
                         (when (= :des @encryption-selection) "is-success"))
              :value "DES"
              :type "button"
              :on-click #(re-frame/dispatch [::events/select-encryption
                                             :des])}]

     [:input {:class (cs "button is-rounded"
                         (when (= :hmac-md5 @encryption-selection) "is-success"))
              :value  "HMAC MD5"
              :type "button"
              :on-click #(re-frame/dispatch [::events/select-encryption
                                             :hmac-md5])}]

     [:input {:class (cs "button is-rounded"
                         (when (= :hmac-sha-1 @encryption-selection) "is-success"))
              :value "HMAC SHA1"
              :type "button"
              :on-click #(re-frame/dispatch [::events/select-encryption
                                             :hmac-sha-1])}]

     [:input {:class (cs "button is-rounded"
                         (when (= :pbkdf2 @encryption-selection) "is-success"))
              :value "PBKDF2"
              :type "button"
              :on-click #(re-frame/dispatch [::events/select-encryption
                                             :pbkdf2])}]]))


(defn encrypt
  []
  (let [phone-number (re-frame/subscribe [::subs/phone-number])
        secret-message (re-frame/subscribe [::subs/secret-message])
        message (re-frame/subscribe [::subs/message])]
    [:div.container
     [tab-bar]
     [:div.columns
      [:div.column
       [:div.columns

        [:div.column
         [:div.field
          [:label.label "Phone Number"]
          [:div.control
           [:input.input {:type "text"
                          :placeholder "+254XXXXXXXX"
                          :on-change #(re-frame/dispatch
                                       [::events/set-phone-number
                                        (-> %
                                            .-target
                                            .-value)])
                          :value @phone-number}]]]]

        [:div.column
         [:div.field
          [:label.label "Secret Message"]
          [:div.control
           [:input.input {:type "text"
                          :placeholder "e.g. broccolli"
                          :on-change #(re-frame/dispatch
                                       [::events/set-secret-message
                                        (-> %
                                            .-target
                                            .-value)])
                          :value @secret-message}]]]]

        

        ]


       [:div.field.is-horizontal
        [:div.field-label.is-normal
         [:label.label "Message"]]
        [:div.field-body
         [:div.field
          [:div.control
           [:textarea.textarea
            {:placeholder "could be anything, like a poem"
             :on-change #(re-frame/dispatch [::events/set-message
                                             (-> %
                                                 .-target
                                                 .-value)])
             :value @message}]]]]]


       ]
      [:div.column
       ;; [select-encryption]
       [pick-encryption]
       [text-area "Encrypted Text" "the encrypted text appears here!!"]]]

     [:div.container
      [:div.columns
       [:div.column.is-half.is-offset-6
        [:input.button.is-large.is-primary {:type "button"
                                            :value "send"
                                            :on-click #(js/console.log "pressed!!!")}]]]]]))



(defn decrypt
  []
  [:div.container
   [tab-bar]
   [:div.columns
    [:div.column "decrypt"]
    [:div.column "decrypt"]
    ]])


(defn footer
  []
  [:footer.page-footer "hello" ])

(defn create-post
  []
  (let [post-text (re-frame/subscribe [::subs/post-text])]
    [:div {:class "container"}
     [:div {:class "col s12"}
      [:div {:class "row"}

       [:div {:class " input-field col s8"}
        [:input {:id "post"
                 :type "text"
                 :class "validate"
                 :on-change #(re-frame/dispatch [::events/set-post (-> %
                                                                       .-target
                                                                       .-value)])
                 :value @post-text}]
        [:label {:for "post"} "Post"]]

       [:div {:class "input-field col s4"}
        [:button {:class "btn waves-effect waves-light"
                  :type "submit"
                  :on-click #(re-frame/dispatch [::events/publish-post @post-text])} "Submit Post"
         [:i {:class "material-icons right"} "send"]]]]]
     ]))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:section.hero.is-light.is-bold.is-fullheight
     [:div.hero-body
      [:div.container
       [:h1.title (str "Hello from " @name ". This is the Home Page.")]
       [:h2.subtitle "project by Kevin K. Mungai"]
       

       [:div
        [:a.is-button.is-outlined {:href "#"}
         [:span.icon [:i.fab.fa-github]]
         [:span "Github"]]]
       
       [:div
        [:a.is-button.is-black.is-medium {:href "#/encrypt"}
         "Let's encrypt something"]]

       ]]]))


  ;; about
  

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:a {:href "#/about"}]
   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    ;; :about-panel [about-panel]
    ;; :home-panel [encrypt]
    :encrypt-panel [encrypt]
    :decrypt-panel [decrypt]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
