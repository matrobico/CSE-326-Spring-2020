class AddKeyToMessages < ActiveRecord::Migration[6.0]
  def change
    add_column :messages, :key, :boolean
  end
end
