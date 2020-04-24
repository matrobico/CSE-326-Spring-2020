class MessagesController < ApplicationController
  before_action :authenticate_request  
  before_action :set_group
  before_action :set_message, only: [:show, :update, :destroy]

  def index
      render json: @group.messages.all

    end
   
    def show
      @message = Messages.find(params[:id])
      
    end
   
    def edit
      @message = Message.find(params[:id])
    end
   
    def create
      @message = @group.messages.create!(message_params)

      render html: @message
    end
   
    def update
      @message = Message.find(params[:id])
   
      if @message.update(message_params)
        redirect_to @message
      else
        render 'edit'
      end
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

      def set_group
        @group = Group.find(params[:group_id])
      end

      def set_message
        @messages = @group.messages.find_by!(id: params[:id])
      end
    end