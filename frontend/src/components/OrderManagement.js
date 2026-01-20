import React, { useState, useEffect } from 'react';
import api from '../api';

function OrderManagement() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [editingOrder, setEditingOrder] = useState(null);
    const [formData, setFormData] = useState({
        customerId: '',
        customerName: '',
        customerEmail: '',
        shippingAddress: '',
        status: 'PENDING',
        notes: '',
        items: [{ productName: '', quantity: 1, price: 0 }],
    });

    useEffect(() => {
        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        try {
            setLoading(true);
            const response = await api.get('/api/orders');
            setOrders(response.data);
            setError('');
        } catch (err) {
            setError('Failed to fetch orders: ' + (err.response?.data?.message || err.message));
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const orderData = {
                ...formData,
                items: formData.items.map(item => ({
                    ...item,
                    productId: `PROD-${Date.now()}`,
                    quantity: parseInt(item.quantity),
                    price: parseFloat(item.price),
                })),
            };

            if (editingOrder) {
                await api.put(`/api/orders/${editingOrder.id}`, orderData);
            } else {
                await api.post('/api/orders', orderData);
            }
            setShowModal(false);
            resetForm();
            fetchOrders();
        } catch (err) {
            setError('Failed to save order: ' + (err.response?.data?.message || err.message));
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this order?')) {
            try {
                await api.delete(`/api/orders/${id}`);
                fetchOrders();
            } catch (err) {
                setError('Failed to delete order: ' + (err.response?.data?.message || err.message));
            }
        }
    };

    const handleEdit = (order) => {
        setEditingOrder(order);
        setFormData({
            customerId: order.customerId,
            customerName: order.customerName,
            customerEmail: order.customerEmail || '',
            shippingAddress: order.shippingAddress || '',
            status: order.status,
            notes: order.notes || '',
            items: order.items || [{ productName: '', quantity: 1, price: 0 }],
        });
        setShowModal(true);
    };

    const resetForm = () => {
        setEditingOrder(null);
        setFormData({
            customerId: '',
            customerName: '',
            customerEmail: '',
            shippingAddress: '',
            status: 'PENDING',
            notes: '',
            items: [{ productName: '', quantity: 1, price: 0 }],
        });
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleItemChange = (index, field, value) => {
        const newItems = [...formData.items];
        newItems[index][field] = value;
        setFormData({ ...formData, items: newItems });
    };

    const addItem = () => {
        setFormData({
            ...formData,
            items: [...formData.items, { productName: '', quantity: 1, price: 0 }],
        });
    };

    const removeItem = (index) => {
        const newItems = formData.items.filter((_, i) => i !== index);
        setFormData({ ...formData, items: newItems });
    };

    if (loading) return <div className="loading">Loading orders...</div>;

    return (
        <div className="card">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Order Management</h2>
                <button
                    className="btn"
                    onClick={() => {
                        resetForm();
                        setShowModal(true);
                    }}
                >
                    Create New Order
                </button>
            </div>

            {error && <div className="error">{error}</div>}

            <div className="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Order Number</th>
                            <th>Customer</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((order) => (
                            <tr key={order.id}>
                                <td>{order.orderNumber}</td>
                                <td>{order.customerName}</td>
                                <td>{order.items?.length || 0}</td>
                                <td>${order.totalAmount?.toFixed(2) || '0.00'}</td>
                                <td>
                                    <span style={{
                                        padding: '0.25rem 0.75rem',
                                        borderRadius: '5px',
                                        background: order.status === 'DELIVERED' ? '#28a745' :
                                            order.status === 'CANCELLED' ? '#dc3545' : '#ffc107',
                                        color: 'white',
                                        fontSize: '0.85rem',
                                    }}>
                                        {order.status}
                                    </span>
                                </td>
                                <td className="action-buttons">
                                    <button className="btn btn-secondary" onClick={() => handleEdit(order)}>
                                        Edit
                                    </button>
                                    <button className="btn btn-danger" onClick={() => handleDelete(order.id)}>
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {showModal && (
                <div className="modal-overlay" onClick={() => setShowModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h3>{editingOrder ? 'Edit Order' : 'Create New Order'}</h3>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Customer ID *</label>
                                <input
                                    type="text"
                                    name="customerId"
                                    value={formData.customerId}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Customer Name *</label>
                                <input
                                    type="text"
                                    name="customerName"
                                    value={formData.customerName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Customer Email</label>
                                <input
                                    type="email"
                                    name="customerEmail"
                                    value={formData.customerEmail}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="form-group">
                                <label>Shipping Address</label>
                                <textarea
                                    name="shippingAddress"
                                    value={formData.shippingAddress}
                                    onChange={handleChange}
                                    rows="2"
                                />
                            </div>
                            <div className="form-group">
                                <label>Status *</label>
                                <select name="status" value={formData.status} onChange={handleChange}>
                                    <option value="PENDING">PENDING</option>
                                    <option value="CONFIRMED">CONFIRMED</option>
                                    <option value="SHIPPED">SHIPPED</option>
                                    <option value="DELIVERED">DELIVERED</option>
                                    <option value="CANCELLED">CANCELLED</option>
                                </select>
                            </div>

                            <h4 style={{ marginTop: '1.5rem', marginBottom: '1rem' }}>Order Items</h4>
                            {formData.items.map((item, index) => (
                                <div key={index} style={{ border: '1px solid #e0e0e0', padding: '1rem', marginBottom: '1rem', borderRadius: '5px' }}>
                                    <div className="form-group">
                                        <label>Product Name *</label>
                                        <input
                                            type="text"
                                            value={item.productName}
                                            onChange={(e) => handleItemChange(index, 'productName', e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                        <div className="form-group">
                                            <label>Quantity *</label>
                                            <input
                                                type="number"
                                                min="1"
                                                value={item.quantity}
                                                onChange={(e) => handleItemChange(index, 'quantity', e.target.value)}
                                                required
                                            />
                                        </div>
                                        <div className="form-group">
                                            <label>Price *</label>
                                            <input
                                                type="number"
                                                step="0.01"
                                                min="0"
                                                value={item.price}
                                                onChange={(e) => handleItemChange(index, 'price', e.target.value)}
                                                required
                                            />
                                        </div>
                                    </div>
                                    {formData.items.length > 1 && (
                                        <button
                                            type="button"
                                            className="btn btn-danger"
                                            onClick={() => removeItem(index)}
                                        >
                                            Remove Item
                                        </button>
                                    )}
                                </div>
                            ))}

                            <button type="button" className="btn btn-secondary" onClick={addItem}>
                                Add Item
                            </button>

                            <div className="form-group">
                                <label>Notes</label>
                                <textarea
                                    name="notes"
                                    value={formData.notes}
                                    onChange={handleChange}
                                    rows="3"
                                />
                            </div>

                            <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
                                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn">
                                    {editingOrder ? 'Update' : 'Create'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default OrderManagement;
