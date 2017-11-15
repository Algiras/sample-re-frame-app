(ns exercize.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn get-value [v]
  (-> v .-target .-value))

(defn get-keyword-value [v]
  (keyword (get-value v)))

(defn listFilterTypes []
  (let [filter-types (rf/subscribe [:filter-types])
        filter-type (rf/subscribe [:filter-type])]
    [:div.form-group.row>col-lg-12
     [:label.col-form-label.col-sm-3 {:for "filter-type"} "Filter Type:"]
     [:div.col-sm-9>select.form-control
      {:value @filter-type
       :id "filter-type"
       :on-change #(rf/dispatch [:set-filter-type (get-keyword-value %)])}
      (for [type @filter-types]
        ^{:key (:type type)}
        [:option {:value (:type type)} (:name type)])]]))

(defn orderTypes []
  (let [order-by-types (rf/subscribe [:order-by-types])
        order-types (rf/subscribe [:order-types])
        order-by (rf/subscribe [:order-by])]
    [:span
     [:div.form-group.row>col-lg-12
      [:label.col-form-label.col-sm-3  {:for "filter-type"} "Order Type:"]
      [:div.col-sm-9>select.form-control
       {:value (:type @order-by)
        :on-change #(rf/dispatch [:set-order-by (get-keyword-value %) (:method @order-by)])}
       (for [order-by-type @order-by-types]
         ^{:key (:type order-by-type)}
         [:option {:value (:type order-by-type)} (:name order-by-type)])]]
     [:div.form-group.row>col-lg-12
      [:label.col-form-label.col-sm-3  {:for "filter-type"} "Order Method:"]
      [:div.col-sm-9>select.form-control
       {:value (:method @order-by)
        :on-change #(rf/dispatch [:set-order-by (:type @order-by) (get-keyword-value %)])}
       (for [order-type @order-types]
         ^{:key (:method order-type)}
         [:option {:value (:method order-type)} (:name order-type)])]
      ]]))


(defn listFilter []
  (r/with-let [collapsed? (r/atom true)]
    [:div
     [:div.row>div.col-lg-12.input-group.filter-input
      (let [filter (rf/subscribe [:filter])
            is-loading? (rf/subscribe [:is-loading?])
            filter-type-name (rf/subscribe [:filter-type-name])] 
        [:input.form-control {:type "text"
                              :value @filter
                              :auto-focus true
                              :disabled @is-loading?
                              :placeholder (str "Filter by " @filter-type-name)
                              :on-change #(rf/dispatch [:set-filter (get-value %)])}])
      [:span.input-group-btn>button.btn.btn-info {:type "button"
                                                  :on-click #(swap! collapsed? not)}
       [:i.fa.fa-cogs {:aria-hidden true}]]]
     (when (not @collapsed?)
       [:div
        [listFilterTypes]
        [orderTypes]])]))


(defn userList []
  (let [users @(rf/subscribe [:filtered-users])
             is-loading? (rf/subscribe [:is-loading?])]
     [:div
      [listFilter]
      [:div.row>div.col-lg-12
       (if (not (empty? users))
         [:ul.list-group.users
          (for [user users]
            ^{:key (user :id)} [:li.list-group-item 
                                [:span.user-name (user :first_name) " " (user :last_name)]
                                [:span.user-email "("(user :email) ")"]
                                [:span.user-gender (if (= (user :gender) "Male")
                                                     [:span.tag-pill.tag.tag-primary
                                                      [:i.fa.fa-male {:aria-hidden true}]]
                                                     [:span.tag-pill.tag.tag-info
                                                      [:i.fa.fa-female {:aria-hidden true}]])]
                                [:a.btn.btn-success.pull-right {:href (str "#/" (user :id))
                                     :role "button"} [:i.fa.fa-eye]]])]
         (if (not @is-loading?)
           [:div.row>div.col-lg-12.text-lg-center.no-results
            "No Users match your search query"]
           [:div.row>div.col-lg-12.text-lg-center.no-results
            [:i.fa.fa-spinner.fa-pulse.fa-3x.fa-fw]]))]]))

(defn input-form-field [id, name, value]
 [:div.form-group.row
       [:label.col-form-label.col-sm-2 {:for id} name ":"]
       [:div.col-sm-10>input.form-control {:id id
                                           :value value
                                           :disabled true}]])

(defn input-form-select-field [id, name, options, value]
  [:div.form-group.row
   [:label.col-form-label.col-sm-2 {:for id} name ":"]
   [:div.col-sm-10>select.form-control {:id id
                                        :value value
                                        :disabled true}
    (for [option options]
      ^{:key option} [:option {:value option} option])]])


(defn friend-info-entry [name value]
  [:div.friend-info [:span.label name ":"] value])


(defn userView []
  (when-let [user @(rf/subscribe [:user-info])]
    [:div.user-info
     [:div.row>div.col-lg-12>ul.breadcrumb
      [:li.breadcrumb-item>a {:href "#/"} "Users"]
      [:li.breadcrumb-item.active (:id user)]]
     [:div.row>div.col-lg-12>form.form-horizontal
      [input-form-field "first_name" "First Name" (:first_name user)]
      [input-form-field "last_name" "Last Name" (:last_name user)]
      [input-form-field "email" "Email" (:email user)]
      [input-form-select-field "gender" "Gender" ["Male" "Female"] (:gender user)]]
     
     [:div.row>div.col-lg-12>div.friends
         (for [friend (:friends user)]
           ^{:key (:email friend)}
           [:div.media
            [:a.media-left>img.media-object {:src (:picture friend)
                                             :alt (:email friend)}]
            [:div.media-body
             [:div.media-header [:h4 (:first_name friend) " " (:last_name friend)]]
             [:div
              [friend-info-entry "First Name" (:first_name friend)]
              [friend-info-entry "Last Name" (:last_name friend)]
              [friend-info-entry "Email" (:email friend)]]]])]]))


(defn footer []
  (when-let [is-loading? (not @(rf/subscribe [:is-loading?]))]
    [:footer.container>div.row>div.col-lg-12>div.footer "2017 Magic Inc."]))

(defn home-page []
  [:div
   [:div.container.user-list
    [userList]]
   [footer]])

(defn user-page []
  [:div
   [:div.container
    [userView]]
   [footer]])

