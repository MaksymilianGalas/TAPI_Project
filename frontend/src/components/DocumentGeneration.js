import React, { useState } from 'react';
import api from '../api';

function DocumentGeneration() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const generateDocument = async (endpoint, filename, data = {}) => {
        try {
            setLoading(true);
            setError('');
            setSuccess('');

            const response = await api.post(endpoint, data, {
                responseType: 'blob',
            });

            // Create download link
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.remove();

            setSuccess(`${filename} generated successfully!`);
        } catch (err) {
            setError('Failed to generate document: ' + (err.response?.data?.message || err.message));
        } finally {
            setLoading(false);
        }
    };

    const handleGenerateInvoicePdf = () => {
        const data = {
            invoiceNumber: 'INV-' + Date.now(),
            customerName: 'John Doe',
            customerEmail: 'john.doe@example.com',
            customerAddress: '123 Main St, City, Country',
            items: 'Product A|2|100.00|200.00;Product B|1|150.00|150.00',
            totalAmount: '350.00',
        };
        generateDocument('/api/documents/generate/pdf/invoice', `invoice_${Date.now()}.pdf`, data);
    };

    const handleGenerateReportPdf = () => {
        const data = {
            reportTitle: 'Monthly Business Report',
            summary: 'This report provides an overview of business performance for the current month.',
            totalOrders: '125',
            totalRevenue: '15750.00',
            activeUsers: '48',
        };
        generateDocument('/api/documents/generate/pdf/report', `report_${Date.now()}.pdf`, data);
    };

    const handleGenerateOrderExcel = () => {
        const data = {
            orders: 'ORD-001|John Doe|3|500.00|CONFIRMED|2024-01-20;ORD-002|Jane Smith|2|350.00|PENDING|2024-01-21',
            totalOrders: '2',
            totalRevenue: '850.00',
        };
        generateDocument('/api/documents/generate/excel/orders', `orders_${Date.now()}.xlsx`, data);
    };

    const handleGenerateUserExcel = () => {
        const data = {
            users: 'john.doe|john@example.com|John|Doe|Active|2024-01-15;jane.smith|jane@example.com|Jane|Smith|Active|2024-01-18',
            totalUsers: '2',
            activeUsers: '2',
        };
        generateDocument('/api/documents/generate/excel/users', `users_${Date.now()}.xlsx`, data);
    };

    return (
        <div className="card">
            <h2>Document Generation</h2>
            <p style={{ marginBottom: '2rem', color: '#666' }}>
                Generate PDF and Excel documents with sample data
            </p>

            {error && <div className="error">{error}</div>}
            {success && <div className="success">{success}</div>}

            <div className="grid">
                <div style={{ border: '2px solid #667eea', borderRadius: '10px', padding: '1.5rem' }}>
                    <h3 style={{ color: '#667eea', marginBottom: '1rem' }}>📄 PDF Documents</h3>

                    <div style={{ marginBottom: '1.5rem' }}>
                        <h4 style={{ marginBottom: '0.5rem' }}>Invoice PDF</h4>
                        <p style={{ fontSize: '0.9rem', color: '#666', marginBottom: '1rem' }}>
                            Generate a professional invoice with customer details and itemized products
                        </p>
                        <button
                            className="btn"
                            onClick={handleGenerateInvoicePdf}
                            disabled={loading}
                        >
                            {loading ? 'Generating...' : 'Generate Invoice PDF'}
                        </button>
                    </div>

                    <div>
                        <h4 style={{ marginBottom: '0.5rem' }}>Business Report PDF</h4>
                        <p style={{ fontSize: '0.9rem', color: '#666', marginBottom: '1rem' }}>
                            Generate a business report with statistics and summary
                        </p>
                        <button
                            className="btn"
                            onClick={handleGenerateReportPdf}
                            disabled={loading}
                        >
                            {loading ? 'Generating...' : 'Generate Report PDF'}
                        </button>
                    </div>
                </div>

                <div style={{ border: '2px solid #28a745', borderRadius: '10px', padding: '1.5rem' }}>
                    <h3 style={{ color: '#28a745', marginBottom: '1rem' }}>📊 Excel Reports</h3>

                    <div style={{ marginBottom: '1.5rem' }}>
                        <h4 style={{ marginBottom: '0.5rem' }}>Order Report Excel</h4>
                        <p style={{ fontSize: '0.9rem', color: '#666', marginBottom: '1rem' }}>
                            Generate an Excel spreadsheet with order data and summary
                        </p>
                        <button
                            className="btn btn-success"
                            onClick={handleGenerateOrderExcel}
                            disabled={loading}
                        >
                            {loading ? 'Generating...' : 'Generate Order Report'}
                        </button>
                    </div>

                    <div>
                        <h4 style={{ marginBottom: '0.5rem' }}>User Report Excel</h4>
                        <p style={{ fontSize: '0.9rem', color: '#666', marginBottom: '1rem' }}>
                            Generate an Excel spreadsheet with user data and statistics
                        </p>
                        <button
                            className="btn btn-success"
                            onClick={handleGenerateUserExcel}
                            disabled={loading}
                        >
                            {loading ? 'Generating...' : 'Generate User Report'}
                        </button>
                    </div>
                </div>
            </div>

            <div className="card" style={{ marginTop: '2rem', background: '#f8f9fa' }}>
                <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>ℹ️ Information</h3>
                <ul style={{ lineHeight: '1.8', color: '#666' }}>
                    <li>All documents are generated with sample data for demonstration</li>
                    <li>PDF documents use iText library for professional formatting</li>
                    <li>Excel documents use Apache POI for spreadsheet generation</li>
                    <li>Documents are generated server-side and downloaded automatically</li>
                    <li>Generation metadata is stored in MongoDB for tracking</li>
                </ul>
            </div>
        </div>
    );
}

export default DocumentGeneration;
