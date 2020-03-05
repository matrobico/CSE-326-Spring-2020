class MessagesController < ApplicationController
skip_before_action :verify_authenticity_token

  def index
    @messages = Message.all
  end

  def show
    @message = Message.find(params[:id])
  end

  def new
  end

  def edit
    @message = Message.find(params[:id])
  end 

  def create
    @message = Message.new(message_params)
 
    @message.save
    redirect_to @message
  end

  def destroy
    @message = Message.find(params[:id])
    @message.destroy
 
    redirect_to messages_path
  end

private
  def message_params
    params.require(:message).permit(:title, :text)
  end

end
