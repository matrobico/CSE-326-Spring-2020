Rails.application.routes.draw do
  get 'user/new'
  get 'user/create'
  post 'authenticate', to: 'authentication#authenticate'
  get 'search', :to => 'groups#search'


  resources :user do
    get "groups", :to => 'user#groups'
  end  
  resources :groups do
    get 'users', :to => 'groups#users'
    post 'adduser', :to => 'groups#adduser'
    resources :messages
  end

end
