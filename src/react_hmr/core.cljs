(ns react-hmr.core
  (:require [devcards.core :as dc :include-macros true]
            [hiccup-next.react :refer [hiccup-element]]
            ["react" :as react]
            [cljs-bean.core :refer [bean]]
            [react-hmr.fresh :refer [defnc]]))


;; Restart Devcards on each hot load
(defn ^:dev/after-load ^:export start []
  (dc/start-devcard-ui!))

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


;; render in the devcards UI
(dc/defcard my-first-component
  #h/n [MyFirstComponent {:name "joe"}])



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

(dc/defcard my-stateful-component
  #h/n [MyStatefulComponent])


;;
;; Using the `defnc` macro
;;

(defnc MyFreshComponent [props]
  (let [[name set-name] (react/useState "fresh")]
    #h/n [:div
          [:div "Hello, " name]
          [:div [:input {:type "text"
                         :value name
                         :on-change #(set-name (.. % -target -value))}]]]))

(dc/defcard my-fresh-component
  #h/n [MyFreshComponent])


;;
;;
;; Editing this file will automatically hot-load your code in the browser
;;
;; Try changing the state of the "fresh" component, then touch the file to cause
;; a reload.
;;
;;
