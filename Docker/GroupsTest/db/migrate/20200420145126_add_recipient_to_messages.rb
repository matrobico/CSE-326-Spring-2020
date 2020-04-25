class AddRecipientToMessages < ActiveRecord::Migration[6.0]
  def change
    add_column :messages, :recipient, :string
  end
end
