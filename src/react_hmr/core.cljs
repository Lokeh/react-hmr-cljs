(ns react-hmr.core
  (:require ;; [devcards.core :as dc :include-macros true]
            [hiccup-next.react :refer [hiccup-element]]
            ["react" :as react]
            ["react-dom" :as react-dom]
            [cljs-bean.core :refer [bean]]
            [react-hmr.fresh :as fresh :refer [defnc]]))


;; Make printing React elements work
(when (exists? js/Symbol)
  (extend-protocol IPrintWithWriter
    js/Symbol
    (-pr-writer [sym writer _]
      (-write writer (str "\"" (.toString sym) "\"")))))




;;
;; Example component
;;

(defn MyFirstComponent [props]
  ;; wrap props in a `bean` to access and destructure like a Clojure map
  (let [{:keys [name]} (bean props)] 
    ;; return hiccup, which is like JSX - gets processed into React elements
    #h/n [:div "Hello, " name]))



;;
;; Hooks
;;

(defn MyStatefulComponent [props]
  (let [[name set-name] (react/useState "jane")]
    #h/n [:div
          [:div "Hello, " name]
          [:div [:input {:type "text"
                         :value name
                         :on-change #(set-name (.. % -target -value))}]]]))



;;
;; Using the Hot Reloadable `defnc` macro
;;

(defnc MyFreshComponent [props]
  (let [[count set-count] (react/useState 0)
        [name set-name] (react/useState "React")]
    (react/useEffect (fn []
                       (let [interval (js/setInterval
                                       #(set-count inc)
                                       1000)]
                         #(js/clearInterval interval))
                       #js []))
    ;; kept it's state!
    #h/n [:div {:style {:background "pink"}}
          [:div "Hello, " name]
          [:div [:button {:on-click #(set-count inc)} "+ " count]]
          [:div [:input {:type "text"
                         :value name
                         :on-change #(set-name (.. % -target -value))}]]]))


;;
;;
;; Editing this file will automatically hot-load your code in the browser
;;
;; Try changing the state of the "fresh" component, then touch the file to cause
;; a reload.
;;
;;

(defn start []
  (react-dom/render #h/n [MyFreshComponent]
                    (. js/document getElementById "app")))

(defn ^:dev/after-load reload []
  (fresh/reload))
