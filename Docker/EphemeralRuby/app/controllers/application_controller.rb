class ApplicationController < ActionController::Base
  def default_url_options
    if Rails.env.development?
#      {:host => "eph.nopesled.com"}
       {:host => "127.0.0.2"}
    else  
      {}
    end
  end

  def show
    @message = Message.find(params[:id])
  end

  def new
  end

end
